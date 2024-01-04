import {Component, OnInit} from '@angular/core';
import {MaterialModule} from "../material/material.module";
import {FlexLayoutModule} from "@angular/flex-layout";
import {HttpClient} from "@angular/common/http";
import {Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [MaterialModule, FlexLayoutModule, RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  username = '';

  constructor(private http: HttpClient, private router: Router) {
  }

  ngOnInit(): void {
    this.http.get<{ username: string }>('/api/v1/users/me').subscribe({
      next: value => (this.username = value.username),
      error: (err) => console.log(err),
    });
  }

  onLogout() {
    this.http.post('/logout', {}).subscribe()
  }
}
