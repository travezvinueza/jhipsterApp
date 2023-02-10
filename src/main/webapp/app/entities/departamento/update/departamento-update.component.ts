import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DepartamentoFormService, DepartamentoFormGroup } from './departamento-form.service';
import { IDepartamento } from '../departamento.model';
import { DepartamentoService } from '../service/departamento.service';
import { IDireccion } from 'app/entities/direccion/direccion.model';
import { DireccionService } from 'app/entities/direccion/service/direccion.service';

@Component({
  selector: 'jhi-departamento-update',
  templateUrl: './departamento-update.component.html',
})
export class DepartamentoUpdateComponent implements OnInit {
  isSaving = false;
  departamento: IDepartamento | null = null;

  direccionsSharedCollection: IDireccion[] = [];

  editForm: DepartamentoFormGroup = this.departamentoFormService.createDepartamentoFormGroup();

  constructor(
    protected departamentoService: DepartamentoService,
    protected departamentoFormService: DepartamentoFormService,
    protected direccionService: DireccionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDireccion = (o1: IDireccion | null, o2: IDireccion | null): boolean => this.direccionService.compareDireccion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departamento }) => {
      this.departamento = departamento;
      if (departamento) {
        this.updateForm(departamento);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const departamento = this.departamentoFormService.getDepartamento(this.editForm);
    if (departamento.id !== null) {
      this.subscribeToSaveResponse(this.departamentoService.update(departamento));
    } else {
      this.subscribeToSaveResponse(this.departamentoService.create(departamento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartamento>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(departamento: IDepartamento): void {
    this.departamento = departamento;
    this.departamentoFormService.resetForm(this.editForm, departamento);

    this.direccionsSharedCollection = this.direccionService.addDireccionToCollectionIfMissing<IDireccion>(
      this.direccionsSharedCollection,
      departamento.direccion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.direccionService
      .query()
      .pipe(map((res: HttpResponse<IDireccion[]>) => res.body ?? []))
      .pipe(
        map((direccions: IDireccion[]) =>
          this.direccionService.addDireccionToCollectionIfMissing<IDireccion>(direccions, this.departamento?.direccion)
        )
      )
      .subscribe((direccions: IDireccion[]) => (this.direccionsSharedCollection = direccions));
  }
}
