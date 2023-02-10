import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PaisFormService, PaisFormGroup } from './pais-form.service';
import { IPais } from '../pais.model';
import { PaisService } from '../service/pais.service';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';

@Component({
  selector: 'jhi-pais-update',
  templateUrl: './pais-update.component.html',
})
export class PaisUpdateComponent implements OnInit {
  isSaving = false;
  pais: IPais | null = null;

  regionsSharedCollection: IRegion[] = [];

  editForm: PaisFormGroup = this.paisFormService.createPaisFormGroup();

  constructor(
    protected paisService: PaisService,
    protected paisFormService: PaisFormService,
    protected regionService: RegionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareRegion = (o1: IRegion | null, o2: IRegion | null): boolean => this.regionService.compareRegion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pais }) => {
      this.pais = pais;
      if (pais) {
        this.updateForm(pais);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pais = this.paisFormService.getPais(this.editForm);
    if (pais.id !== null) {
      this.subscribeToSaveResponse(this.paisService.update(pais));
    } else {
      this.subscribeToSaveResponse(this.paisService.create(pais));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPais>>): void {
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

  protected updateForm(pais: IPais): void {
    this.pais = pais;
    this.paisFormService.resetForm(this.editForm, pais);

    this.regionsSharedCollection = this.regionService.addRegionToCollectionIfMissing<IRegion>(this.regionsSharedCollection, pais.region);
  }

  protected loadRelationshipsOptions(): void {
    this.regionService
      .query()
      .pipe(map((res: HttpResponse<IRegion[]>) => res.body ?? []))
      .pipe(map((regions: IRegion[]) => this.regionService.addRegionToCollectionIfMissing<IRegion>(regions, this.pais?.region)))
      .subscribe((regions: IRegion[]) => (this.regionsSharedCollection = regions));
  }
}
