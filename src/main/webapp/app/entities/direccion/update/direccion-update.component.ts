import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DireccionFormService, DireccionFormGroup } from './direccion-form.service';
import { IDireccion } from '../direccion.model';
import { DireccionService } from '../service/direccion.service';
import { IPais } from 'app/entities/pais/pais.model';
import { PaisService } from 'app/entities/pais/service/pais.service';

@Component({
  selector: 'jhi-direccion-update',
  templateUrl: './direccion-update.component.html',
})
export class DireccionUpdateComponent implements OnInit {
  isSaving = false;
  direccion: IDireccion | null = null;

  paisSharedCollection: IPais[] = [];

  editForm: DireccionFormGroup = this.direccionFormService.createDireccionFormGroup();

  constructor(
    protected direccionService: DireccionService,
    protected direccionFormService: DireccionFormService,
    protected paisService: PaisService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePais = (o1: IPais | null, o2: IPais | null): boolean => this.paisService.comparePais(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ direccion }) => {
      this.direccion = direccion;
      if (direccion) {
        this.updateForm(direccion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const direccion = this.direccionFormService.getDireccion(this.editForm);
    if (direccion.id !== null) {
      this.subscribeToSaveResponse(this.direccionService.update(direccion));
    } else {
      this.subscribeToSaveResponse(this.direccionService.create(direccion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDireccion>>): void {
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

  protected updateForm(direccion: IDireccion): void {
    this.direccion = direccion;
    this.direccionFormService.resetForm(this.editForm, direccion);

    this.paisSharedCollection = this.paisService.addPaisToCollectionIfMissing<IPais>(this.paisSharedCollection, direccion.pais);
  }

  protected loadRelationshipsOptions(): void {
    this.paisService
      .query()
      .pipe(map((res: HttpResponse<IPais[]>) => res.body ?? []))
      .pipe(map((pais: IPais[]) => this.paisService.addPaisToCollectionIfMissing<IPais>(pais, this.direccion?.pais)))
      .subscribe((pais: IPais[]) => (this.paisSharedCollection = pais));
  }
}
