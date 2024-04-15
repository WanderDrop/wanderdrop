import { Injectable } from '@angular/core';
import { Attraction } from './attraction.model';

@Injectable({
  providedIn: 'root',
})
export class AttractionService {
  private attraction: Attraction;

  constructor() {
    this.attraction = new Attraction(
      1,
      'Mystic Mountain Adventure',
      'Embark on an exhilarating journey through Mystic Mountain, where lush forests, cascading waterfalls, and breathtaking vistas await. Traverse winding trails, brave suspension bridges, and discover hidden caves teeming with ancient mysteries. With thrilling zip lines and heart-pounding rappelling, this adventure promises unforgettable experiences for thrill-seekers and nature lovers alike.',
      'John Doe'
    );
  }

  getAttraction(): Attraction {
    return this.attraction;
  }

  getAttractionId(): number {
    return this.attraction.id;
  }
}
