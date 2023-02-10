import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDireccion } from '../direccion.model';

@Component({
  selector: 'jhi-direccion-detail',
  templateUrl: './direccion-detail.component.html',
})
export class DireccionDetailComponent implements OnInit {
  direccion: IDireccion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ direccion }) => {
      this.direccion = direccion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
