import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'questionnaire',
        data: { pageTitle: 'Questionnaires' },
        loadChildren: () => import('./questionnaire/questionnaire.module').then(m => m.QuestionnaireModule),
      },
      {
        path: 'laptop',
        data: { pageTitle: 'Laptops' },
        loadChildren: () => import('./laptop/laptop.module').then(m => m.LaptopModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
