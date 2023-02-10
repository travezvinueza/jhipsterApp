import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersona } from '../persona.model';

@Component({
  selector: 'jhi-persona-detail',
  templateUrl: './persona-detail.component.html',
})
export class PersonaDetailComponent implements OnInit {
  persona: IPersona | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ persona }) => {
      this.persona = persona;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
