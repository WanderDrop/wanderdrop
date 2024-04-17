import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-modify-attraction',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './modify-attraction.component.html',
  styleUrl: './modify-attraction.component.css',
})
export class ModifyAttractionComponent {
  @Input() attractionName: string = '';
  @Input() description: string = '';
  @Output() dataChanged = new EventEmitter<{
    originalName: string;
    attractionName: string;
    description: string;
  }>();
}
