import { Component } from '@angular/core';
import { MatCard } from "@angular/material/card";
import { MatIcon } from "@angular/material/icon";
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-not-fund',
  imports: [MatCard, MatIcon, RouterLink],
  templateUrl: './not-fund.html',
  styleUrl: './not-fund.css',
})
export class NotFund {
  constructor(private router:Router){}

}
