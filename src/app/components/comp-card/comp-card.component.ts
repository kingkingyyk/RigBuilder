import { Component, OnInit, Input, Inject } from '@angular/core';
import { Part, Make, AppComponent } from 'src/app/app.component';

@Component({
  selector: 'comp-card',
  templateUrl: './comp-card.component.html',
  styleUrls: ['./comp-card.component.css']
})
export class CompCardComponent implements OnInit {
  @Input('type') type : string;
  @Input() parts : Part[];
  @Input() makes : Make[];
  selectedPart : Part = null;

  constructor(@Inject(AppComponent) private parent: AppComponent) { }

  ngOnInit(): void {
    setTimeout(() => {
      for (let p of this.parts) if (p.type === this.type) {
        this.selectedPart = p;
        break;
      }
    }, 100);
  }

  onSelectedPartChanges() {
    this.parent.updateTotal();
  }
}
