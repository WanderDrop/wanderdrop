import { AttractionStatus } from './attraction-status.enum';

export class Attraction {
  private _id: number;
  private _name: string;
  private _description: string;
  // private _location: string;
  // private _city?: string;
  // private _country?: string;
  private _createdBy: string; //replace with User if we have user model created
  // private _updatedBy?: User; //probably only user id is needed(number)
  private _createdAt: Date;
  // private _updatedAt?: Date;
  private _status: AttractionStatus;
  // private _deletionReason?: DeletionReason; //probably only deletionReason id is needed(number)

  constructor(
    id?: number,
    name?: string,
    description?: string,
    createdBy?: string
  ) {
    this._id = id ? id : 0;
    this._name = name ? name : '';
    this._description = description ? description : '';
    this._createdBy = createdBy ? createdBy : 'Eleri';
    this._createdAt = new Date();
    this._status = AttractionStatus.Active;
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

  get status(): AttractionStatus {
    return this._status;
  }

  set status(value: AttractionStatus) {
    this._status = value;
  }
}
