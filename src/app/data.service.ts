import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private static componentTypes : string = "https://raw.githubusercontent.com/kingkingyyk/RigBuilder/develop/data/component-types.json";
  private static makes : string = "https://raw.githubusercontent.com/kingkingyyk/RigBuilder/develop/data/makes.json";
  private static components : string = "https://raw.githubusercontent.com/kingkingyyk/RigBuilder/develop/data/components.json";
  private static recommendations : string = "https://github.com/kingkingyyk/RigBuilder/blob/develop/data/recommendations.json";

  constructor(private client : HttpClient) { }
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
