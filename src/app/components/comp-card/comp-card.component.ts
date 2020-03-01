import { Component, OnInit, Input } from '@angular/core';
import { Part, Make } from 'src/app/app.component';

@Component({
  selector: 'comp-card',
  templateUrl: './comp-card.component.html',
  styleUrls: ['./comp-card.component.css']
})
export class CompCardComponent implements OnInit {
  @Input('type') type : string;
  @Input() parts : Part[];
  @Input() makes : Make[];

  constructor() { }

  ngOnInit(): void {
    this.parts = this.parts.filter((part => part.type === this.type));
    this.parts.sort((a,b) => a.price - b.price);
  }

}
