import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILaptop } from '../laptop.model';

@Component({
  selector: 'jhi-laptop-detail',
  templateUrl: './laptop-detail.component.html',
})
export class LaptopDetailComponent implements OnInit {
  laptop: ILaptop | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ laptop }) => {
      this.laptop = laptop;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
