import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDepartamento } from '../departamento.model';

@Component({
  selector: 'jhi-departamento-detail',
  templateUrl: './departamento-detail.component.html',
})
export class DepartamentoDetailComponent implements OnInit {
  departamento: IDepartamento | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departamento }) => {
      this.departamento = departamento;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
