import { Component, OnInit } from '@angular/core';
import { DataService } from './data.service';

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

  constructor(private service : DataService) {}

  ngOnInit(): void {
    this.service.getComponentTypes().subscribe((ct : string[]) => {
      this.componentTypes = ct;
      this.service.getMakes().subscribe((m : Make []) => {
        this.makes = m;
        this.service.getComponents().subscribe((p : Part []) => {
          this.parts = p;
          for (let part of this.parts) part.make = this.locateMake(part.make);
        })
      });
    });
  }

  private locateMake(name : string) : Make {
    for (let make of this.makes) if (make.name === name) return make;
    alert('gg at '+name);
    return null;
  }
}
