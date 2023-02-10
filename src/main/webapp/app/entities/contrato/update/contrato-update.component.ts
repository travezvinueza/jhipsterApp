import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ContratoFormService, ContratoFormGroup } from './contrato-form.service';
import { IContrato } from '../contrato.model';
import { ContratoService } from '../service/contrato.service';
import { ITrabajo } from 'app/entities/trabajo/trabajo.model';
import { TrabajoService } from 'app/entities/trabajo/service/trabajo.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';
import { Idioma } from 'app/entities/enumerations/idioma.model';

@Component({
  selector: 'jhi-contrato-update',
  templateUrl: './contrato-update.component.html',
})
export class ContratoUpdateComponent implements OnInit {
  isSaving = false;
  contrato: IContrato | null = null;
  idiomaValues = Object.keys(Idioma);

  trabajosSharedCollection: ITrabajo[] = [];
  departamentosSharedCollection: IDepartamento[] = [];
  empleadosSharedCollection: IEmpleado[] = [];

  editForm: ContratoFormGroup = this.contratoFormService.createContratoFormGroup();

  constructor(
    protected contratoService: ContratoService,
    protected contratoFormService: ContratoFormService,
    protected trabajoService: TrabajoService,
    protected departamentoService: DepartamentoService,
    protected empleadoService: EmpleadoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTrabajo = (o1: ITrabajo | null, o2: ITrabajo | null): boolean => this.trabajoService.compareTrabajo(o1, o2);

  compareDepartamento = (o1: IDepartamento | null, o2: IDepartamento | null): boolean =>
    this.departamentoService.compareDepartamento(o1, o2);

  compareEmpleado = (o1: IEmpleado | null, o2: IEmpleado | null): boolean => this.empleadoService.compareEmpleado(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contrato }) => {
      this.contrato = contrato;
      if (contrato) {
        this.updateForm(contrato);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contrato = this.contratoFormService.getContrato(this.editForm);
    if (contrato.id !== null) {
      this.subscribeToSaveResponse(this.contratoService.update(contrato));
    } else {
      this.subscribeToSaveResponse(this.contratoService.create(contrato));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContrato>>): void {
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

  protected updateForm(contrato: IContrato): void {
    this.contrato = contrato;
    this.contratoFormService.resetForm(this.editForm, contrato);

    this.trabajosSharedCollection = this.trabajoService.addTrabajoToCollectionIfMissing<ITrabajo>(
      this.trabajosSharedCollection,
      contrato.trabajo
    );
    this.departamentosSharedCollection = this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(
      this.departamentosSharedCollection,
      contrato.departamento
    );
    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(
      this.empleadosSharedCollection,
      contrato.empleado
    );
  }

  protected loadRelationshipsOptions(): void {
    this.trabajoService
      .query()
      .pipe(map((res: HttpResponse<ITrabajo[]>) => res.body ?? []))
      .pipe(map((trabajos: ITrabajo[]) => this.trabajoService.addTrabajoToCollectionIfMissing<ITrabajo>(trabajos, this.contrato?.trabajo)))
      .subscribe((trabajos: ITrabajo[]) => (this.trabajosSharedCollection = trabajos));

    this.departamentoService
      .query()
      .pipe(map((res: HttpResponse<IDepartamento[]>) => res.body ?? []))
      .pipe(
        map((departamentos: IDepartamento[]) =>
          this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(departamentos, this.contrato?.departamento)
        )
      )
      .subscribe((departamentos: IDepartamento[]) => (this.departamentosSharedCollection = departamentos));

    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) =>
          this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(empleados, this.contrato?.empleado)
        )
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));
  }
}
