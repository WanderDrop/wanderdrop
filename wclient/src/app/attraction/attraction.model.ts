export class Attraction {
  private _id: number;
  private _name: string;
  private _description: string;
  // private _location: string;
  // private _city?: string;
  // private _country?: string;
  private _createdBy: string; //replace with User if we have user model created
  // private _updatedBy?: User;
  private _createdAt: Date;
  // private _updatedAt?: Date;
  private _status: 'active' | 'deleted';
  // private _deletionReason?: DeletionReason;

  constructor(
    id?: number,
    name?: string,
    description?: string,
    createdBy?: string
  ) {
    this._id = id ? id : 0;
    this._name = name ? name : '';
    this._description = description ? description : '';
    this._createdBy = 'Eleri';
    this._createdAt = new Date();
    this._status = 'active';
  }

  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get description(): string {
    return this._description;
  }

  set description(value: string) {
    this._description = value;
  }

  get createdBy(): string {
    return this._createdBy;
  }

  set createdBy(value: string) {
    this._createdBy = value;
  }
}
