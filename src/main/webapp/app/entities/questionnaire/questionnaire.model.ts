import { ILaptop } from 'app/entities/laptop/laptop.model';
import { IUser } from 'app/entities/user/user.model';

export interface IQuestionnaire {
  id?: number;
  answer1UsedAt?: string | null;
  answer2Type?: string | null;
  answer3Size?: number | null;
  answer4Function?: string | null;
  answer5Price?: number | null;
  model?: ILaptop | null;
  assignedTo?: IUser | null;
}

export class Questionnaire implements IQuestionnaire {
  constructor(
    public id?: number,
    public answer1UsedAt?: string | null,
    public answer2Type?: string | null,
    public answer3Size?: number | null,
    public answer4Function?: string | null,
    public answer5Price?: number | null,
    public model?: ILaptop | null,
    public assignedTo?: IUser | null
  ) {}
}

export function getQuestionnaireIdentifier(questionnaire: IQuestionnaire): number | undefined {
  return questionnaire.id;
}
