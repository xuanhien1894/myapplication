export interface IBook {
  id?: number;
  name?: string;
  year?: number;
  page?: number;
}

export class Book implements IBook {
  constructor(public id?: number, public name?: string, public year?: number, public page?: number) {}
}
