import { AttractionStatus } from './attraction-status.enum';

export class Attraction {
  private _id: number;
  private _name: string;
  private _description: string;
  private _latitude: number;
  private _longitude: number;
  // private _location: string;
  // private _city?: string;
  // private _country?: string;
  private _createdBy: string; //replace with User if we have user model created
  // private _updatedBy?: User; //probably only user id is needed(number)
  private _createdAt: Date;
  // private _updatedAt?: Date;
  private _status: AttractionStatus;
  // private _deletionReason?: DeletionReason; //probably only deletionReason id is needed(number)
  private static lastId = 1;

  constructor(
    name?: string,
    description?: string,
    latitude?: number,
    longitude?: number,
    createdBy?: string
  ) {
    this._id = Attraction.lastId++;
    this._name = name ? name : '';
    this._description = description ? description : '';
    this._latitude = latitude ? latitude : 0;
    this._longitude = longitude ? longitude : 0;
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

  get latitude(): number {
    return this._latitude;
  }

  set latitude(value: number) {
    this._latitude = value;
  }

  get longitude(): number {
    return this._longitude;
  }

  set longitude(value: number) {
    this._longitude = value;
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
