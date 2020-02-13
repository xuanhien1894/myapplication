export interface IEntity1 {
  id?: number;
  name?: string;
  price?: number;
}

export class Entity1 implements IEntity1 {
  constructor(public id?: number, public name?: string, public price?: number) {}
}
