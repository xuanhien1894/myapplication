export interface IEntity2 {
  id?: number;
  name?: string;
  price?: number;
}

export class Entity2 implements IEntity2 {
  constructor(public id?: number, public name?: string, public price?: number) {}
}
