import { AttractionStatus } from './attraction-status.enum';

export class Attraction {
  private _id: number;
  private _name: string;
  private _description: string;
  private _latitude: number;
  private _longitude: number;
  private _createdBy!: number;
  private _updatedBy?: number;
  private _createdAt: Date;
  private _updatedAt?: Date;
  private _status: AttractionStatus;
  private _deletionReason?: number;
  private static lastId = 1;

  constructor(
    name: string,
    description: string,
    latitude: number,
    longitude: number,
    createdBy: number
  ) {
    this._id = Attraction.lastId++;
    this._name = name;
    this._description = description;
    this._latitude = latitude;
    this._longitude = longitude;
    this._createdBy = createdBy;
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

  get createdBy(): number {
    return this._createdBy;
  }

  set createdBy(value: number) {
    this._createdBy = value;
  }

  get updatedBy(): number | undefined {
    return this._updatedBy;
  }

  set updatedBy(value: number | undefined) {
    this._updatedBy = value;
  }

  get updatedAt(): Date | undefined {
    return this._updatedAt;
  }

  set updatedAt(value: Date | undefined) {
    this._updatedAt = value;
  }

  get status(): AttractionStatus {
    return this._status;
  }

  set status(value: AttractionStatus) {
    this._status = value;
  }

  get deletionReason(): number | undefined {
    return this._deletionReason;
  }

  set deletionReason(value: number | undefined) {
    this._deletionReason = value;
  }
}
