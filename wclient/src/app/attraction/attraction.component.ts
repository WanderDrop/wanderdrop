import { Component } from '@angular/core';
import { Attraction } from './attraction.model';
import { CommentItemComponent } from '../comment/comment-list/comment-item/comment-item.component';

@Component({
  selector: 'app-attraction',
  standalone: true,
  templateUrl: './attraction.component.html',
  styleUrl: './attraction.component.css',
  imports: [CommentItemComponent],
})
export class AttractionComponent {
  attraction!: Attraction;

  //create a dummy attraction
  ngOnInit() {
    this.attraction = new Attraction(
      1,
      'Mystic Mountain Adventure',
      'Embark on an exhilarating journey through Mystic Mountain, where lush forests, cascading waterfalls, and breathtaking vistas await. Traverse winding trails, brave suspension bridges, and discover hidden caves teeming with ancient mysteries. With thrilling zip lines and heart-pounding rappelling, this adventure promises unforgettable experiences for thrill-seekers and nature lovers alike.',
      'John Doe'
    );
    console.log(this.attraction);
  }
}
