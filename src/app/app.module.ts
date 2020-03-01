import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatMenuModule } from '@angular/material/menu';
import { ClipboardModule } from '@angular/cdk/clipboard';

import { CompCardComponent } from './components/comp-card/comp-card.component';
import { SummaryDiagComponent } from './components/summary-diag/summary-diag.component';

@NgModule({
  declarations: [
    AppComponent,
    CompCardComponent,
    SummaryDiagComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatCardModule,
    MatToolbarModule,
    MatIconModule,
    MatSelectModule,
    MatButtonModule,
    MatDialogModule,
    MatListModule,
    MatSnackBarModule,
    MatMenuModule,
    ClipboardModule,
  ],
  entryComponents: [
    SummaryDiagComponent,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
