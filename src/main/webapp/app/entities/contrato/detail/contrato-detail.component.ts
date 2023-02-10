import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContrato } from '../contrato.model';

@Component({
  selector: 'jhi-contrato-detail',
  templateUrl: './contrato-detail.component.html',
})
export class ContratoDetailComponent implements OnInit {
  contrato: IContrato | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contrato }) => {
      this.contrato = contrato;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
