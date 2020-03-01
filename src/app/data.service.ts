import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private static commitData : string = "https://api.github.com/repos/kingkingyyk/rigbuilder/commits/master";
  private static componentTypes : string = "https://raw.githubusercontent.com/kingkingyyk/RigBuilder/master/data/component-types.json";
  private static makes : string = "https://raw.githubusercontent.com/kingkingyyk/RigBuilder/master/data/makes.json";
  private static components : string = "https://raw.githubusercontent.com/kingkingyyk/RigBuilder/master/data/components.json";
  private static recommendations : string = "https://raw.githubusercontent.com/kingkingyyk/RigBuilder/master/data/recommendations.json";

  constructor(private client : HttpClient) { }
  getLatestUpdate() {
    return this.client.get(DataService.commitData);
  }
  getComponentTypes() {
    return this.client.get(DataService.componentTypes);
  }
  getMakes() {
    return this.client.get(DataService.makes);
  }
  getComponents() {
    return this.client.get(DataService.components);
  }
  getRecommendations() {
    return this.client.get(DataService.recommendations);
  }
}
