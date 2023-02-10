import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TareaFormService, TareaFormGroup } from './tarea-form.service';
import { ITarea } from '../tarea.model';
import { TareaService } from '../service/tarea.service';

@Component({
  selector: 'jhi-tarea-update',
  templateUrl: './tarea-update.component.html',
})
export class TareaUpdateComponent implements OnInit {
  isSaving = false;
  tarea: ITarea | null = null;

  editForm: TareaFormGroup = this.tareaFormService.createTareaFormGroup();

  constructor(
    protected tareaService: TareaService,
    protected tareaFormService: TareaFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tarea }) => {
      this.tarea = tarea;
      if (tarea) {
        this.updateForm(tarea);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tarea = this.tareaFormService.getTarea(this.editForm);
    if (tarea.id !== null) {
      this.subscribeToSaveResponse(this.tareaService.update(tarea));
    } else {
      this.subscribeToSaveResponse(this.tareaService.create(tarea));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITarea>>): void {
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

  protected updateForm(tarea: ITarea): void {
    this.tarea = tarea;
    this.tareaFormService.resetForm(this.editForm, tarea);
  }
}
