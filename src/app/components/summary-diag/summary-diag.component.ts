import { Component, OnInit, Inject } from '@angular/core';
import { Part } from 'src/app/app.component';
import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface DialogData {
  parts : Part [];
  total : number;
}

@Component({
  selector: 'app-summary-diag',
  templateUrl: './summary-diag.component.html',
  styleUrls: ['./summary-diag.component.css']
})
export class SummaryDiagComponent implements OnInit {
  clipboardText : string = "";

  constructor(public dialogRef : MatDialogRef<SummaryDiagComponent>,
              @Inject(MAT_DIALOG_DATA) public data : DialogData,
              private snackbar : MatSnackBar) { }

  ngOnInit(): void {
    for (let p of this.data.parts) {
      this.clipboardText += p.make.name + ' ' + p.model + ' RM' + p.price + '\n';
    }
    this.clipboardText += 'Total : RM'+this.data.total;
  }

  popupClipboardCopied() {
    this.snackbar.open('Summary copied to clipboard!', null, {
      duration: 2000,
    });
  }

  close(): void {
    this.dialogRef.close();
  }
}
