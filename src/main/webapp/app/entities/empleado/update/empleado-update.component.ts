import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmpleadoFormService, EmpleadoFormGroup } from './empleado-form.service';
import { IEmpleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';

@Component({
  selector: 'jhi-empleado-update',
  templateUrl: './empleado-update.component.html',
})
export class EmpleadoUpdateComponent implements OnInit {
  isSaving = false;
  empleado: IEmpleado | null = null;

  empleadosSharedCollection: IEmpleado[] = [];
  departamentosSharedCollection: IDepartamento[] = [];

  editForm: EmpleadoFormGroup = this.empleadoFormService.createEmpleadoFormGroup();

  constructor(
    protected empleadoService: EmpleadoService,
    protected empleadoFormService: EmpleadoFormService,
    protected departamentoService: DepartamentoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmpleado = (o1: IEmpleado | null, o2: IEmpleado | null): boolean => this.empleadoService.compareEmpleado(o1, o2);

  compareDepartamento = (o1: IDepartamento | null, o2: IDepartamento | null): boolean =>
    this.departamentoService.compareDepartamento(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleado }) => {
      this.empleado = empleado;
      if (empleado) {
        this.updateForm(empleado);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empleado = this.empleadoFormService.getEmpleado(this.editForm);
    if (empleado.id !== null) {
      this.subscribeToSaveResponse(this.empleadoService.update(empleado));
    } else {
      this.subscribeToSaveResponse(this.empleadoService.create(empleado));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpleado>>): void {
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

  protected updateForm(empleado: IEmpleado): void {
    this.empleado = empleado;
    this.empleadoFormService.resetForm(this.editForm, empleado);

    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(
      this.empleadosSharedCollection,
      empleado.inmediatosuperior
    );
    this.departamentosSharedCollection = this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(
      this.departamentosSharedCollection,
      empleado.departamento
    );
  }

  protected loadRelationshipsOptions(): void {
    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) =>
          this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(empleados, this.empleado?.inmediatosuperior)
        )
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));

    this.departamentoService
      .query()
      .pipe(map((res: HttpResponse<IDepartamento[]>) => res.body ?? []))
      .pipe(
        map((departamentos: IDepartamento[]) =>
          this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(departamentos, this.empleado?.departamento)
        )
      )
      .subscribe((departamentos: IDepartamento[]) => (this.departamentosSharedCollection = departamentos));
  }
}
