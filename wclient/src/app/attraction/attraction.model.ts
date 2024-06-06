import { AttractionStatus } from './attraction-status.enum';

export class Attraction {
  private _attractionId!: number;
  private _name: string;
  private _description: string;
  private _latitude: number;
  private _longitude: number;
  private _createdBy!: string;
  private _updatedBy?: string;
  private _createdAt: Date;
  private _updatedAt?: Date;
  private _status: AttractionStatus;
  private _deletionReason?: number;

  constructor(
    name: string,
    description: string,
    latitude: number,
    longitude: number,
    createdBy: string
  ) {
    this._name = name;
    this._description = description;
    this._latitude = latitude;
    this._longitude = longitude;
    this._createdBy = createdBy;
    this._createdAt = new Date();
    this._status = AttractionStatus.Active;
  }

  get id(): number {
    return this._attractionId;
  }

  set id(value: number) {
    this._attractionId = value;
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

  get updatedBy(): string | undefined {
    return this._updatedBy;
  }

  set updatedBy(value: string | undefined) {
    this._updatedBy = value;
  }

  get createdAt(): Date | undefined {
    return this._createdAt;
  }

  set createdAt(value: Date) {
    this._createdAt = value;
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

  toRequestPayload() {
    return {
      name: this._name,
      description: this._description,
      latitude: this._latitude,
      longitude: this._longitude,
    };
  }

  static fromResponse(response: any): Attraction {
    const attraction = new Attraction(
      response.name,
      response.description,
      response.latitude,
      response.longitude,
      response.createdBy
    );
    attraction._attractionId = response.attractionId;
    attraction.createdAt = new Date(response.createdAt);
    return attraction;
  }
}
