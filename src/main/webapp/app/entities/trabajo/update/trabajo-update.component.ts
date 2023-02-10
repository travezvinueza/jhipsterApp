import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TrabajoFormService, TrabajoFormGroup } from './trabajo-form.service';
import { ITrabajo } from '../trabajo.model';
import { TrabajoService } from '../service/trabajo.service';
import { ITarea } from 'app/entities/tarea/tarea.model';
import { TareaService } from 'app/entities/tarea/service/tarea.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';

@Component({
  selector: 'jhi-trabajo-update',
  templateUrl: './trabajo-update.component.html',
})
export class TrabajoUpdateComponent implements OnInit {
  isSaving = false;
  trabajo: ITrabajo | null = null;

  tareasSharedCollection: ITarea[] = [];
  empleadosSharedCollection: IEmpleado[] = [];

  editForm: TrabajoFormGroup = this.trabajoFormService.createTrabajoFormGroup();

  constructor(
    protected trabajoService: TrabajoService,
    protected trabajoFormService: TrabajoFormService,
    protected tareaService: TareaService,
    protected empleadoService: EmpleadoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTarea = (o1: ITarea | null, o2: ITarea | null): boolean => this.tareaService.compareTarea(o1, o2);

  compareEmpleado = (o1: IEmpleado | null, o2: IEmpleado | null): boolean => this.empleadoService.compareEmpleado(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trabajo }) => {
      this.trabajo = trabajo;
      if (trabajo) {
        this.updateForm(trabajo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trabajo = this.trabajoFormService.getTrabajo(this.editForm);
    if (trabajo.id !== null) {
      this.subscribeToSaveResponse(this.trabajoService.update(trabajo));
    } else {
      this.subscribeToSaveResponse(this.trabajoService.create(trabajo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrabajo>>): void {
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

  protected updateForm(trabajo: ITrabajo): void {
    this.trabajo = trabajo;
    this.trabajoFormService.resetForm(this.editForm, trabajo);

    this.tareasSharedCollection = this.tareaService.addTareaToCollectionIfMissing<ITarea>(
      this.tareasSharedCollection,
      ...(trabajo.tareas ?? [])
    );
    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(
      this.empleadosSharedCollection,
      trabajo.empleado
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tareaService
      .query()
      .pipe(map((res: HttpResponse<ITarea[]>) => res.body ?? []))
      .pipe(map((tareas: ITarea[]) => this.tareaService.addTareaToCollectionIfMissing<ITarea>(tareas, ...(this.trabajo?.tareas ?? []))))
      .subscribe((tareas: ITarea[]) => (this.tareasSharedCollection = tareas));

    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) => this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(empleados, this.trabajo?.empleado))
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));
  }
}
