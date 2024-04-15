import { Component } from '@angular/core';
import { Attraction } from './attraction.model';

@Component({
  selector: 'app-attraction',
  standalone: true,
  imports: [],
  templateUrl: './attraction.component.html',
  styleUrl: './attraction.component.css',
})
export class AttractionComponent {
  attraction!: Attraction;
}
