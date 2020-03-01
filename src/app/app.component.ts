import { Component, OnInit, ViewChildren } from '@angular/core';
import { DataService } from './data.service';
import { CompCardComponent } from './components/comp-card/comp-card.component';
import { MatDialog } from '@angular/material/dialog';
import { SummaryDiagComponent } from './components/summary-diag/summary-diag.component';

export class Make {
  name : string;
  logoURL : string;
}
export class Part {
  make: any;
  model : string;
  type: string;
  description : string;
  url : string;
  price : number;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'RigBuilder';
  componentTypes : string [];
  makes : Make [];
  parts : Part [];
  total = 0;
  
  loading = false;
  loadingError = false;

  @ViewChildren(CompCardComponent) cards : CompCardComponent[];

  constructor(private service : DataService,
              private dialog : MatDialog) {}

  ngOnInit(): void {
    this.loading = true;
    this.loadingError = false;
    this.service.getComponentTypes().subscribe((ct : string[]) => {
      this.componentTypes = ct;

      this.service.getMakes().subscribe((m : Make []) => {
        this.makes = m;
        this.makes.push(this.createDummyMake());

        this.service.getComponents().subscribe((p : Part []) => {
          this.parts = p;
          for (let ct of this.componentTypes) this.parts.push(this.createDummyPart(ct));
          for (let part of this.parts) part.make = this.locateMake(part.make);
          this.parts.sort((a,b) => a.price - b.price);

          this.service.getRecommendations().subscribe((data : object) => {
            
          }, error => this.loadingError = true).add(() => this.loading=false);
        }, error => this.loadingError = true);
      }, error => this.loadingError = true);
    }, error => this.loadingError = true);
  }

  updateTotal() : void {
    this.total = 0;
    for (let c of this.cards) this.total += c.selectedPart.price;
  }

  openSummary() : void {
    let p = [];
    for (let ct of this.componentTypes) {
      for (let c of this.cards) if (c.type == ct && c.selectedPart.price > 0) {
        p.push(c.selectedPart);
        break;
      }
    }
    const dialogRef=this.dialog.open(SummaryDiagComponent, {data:
      {parts:p, total: this.total}
    });
  }

  private createDummyMake() : Make {
    let m = new Make();
    m.name = null;
    m.logoURL = null;
    return m;
  }
  private locateMake(name : string) : Make {
    for (let make of this.makes) if (make.name === name) return make;
    return null;
  }
  private createDummyPart(type : string) {
    let c = new Part();
    c.make = null;
    c.model = 'N/A';
    c.type = type;
    c.description = null;
    c.url = null;
    c.price = 0;
    return c;
  }
}
