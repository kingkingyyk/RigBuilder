import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private static componentTypes : string = "";
  private static makes : string = "";
  private static components : string = "";
  private static recommendations : string = "";

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
