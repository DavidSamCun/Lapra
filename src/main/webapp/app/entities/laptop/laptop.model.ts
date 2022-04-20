export interface ILaptop {
  id?: number;
  brandName?: string;
  modelName?: string;
  screenSize?: number | null;
  memory?: number | null;
  storage?: number | null;
  screenBrightness?: number | null;
  screenRefreshHz?: number | null;
  price?: number | null;
}

export class Laptop implements ILaptop {
  constructor(
    public id?: number,
    public brandName?: string,
    public modelName?: string,
    public screenSize?: number | null,
    public memory?: number | null,
    public storage?: number | null,
    public screenBrightness?: number | null,
    public screenRefreshHz?: number | null,
    public price?: number | null
  ) {}
}

export function getLaptopIdentifier(laptop: ILaptop): number | undefined {
  return laptop.id;
}
