import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'internet-forum-ui';
  me = '';
  admin = false;
  user = false;

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {

    this.http.get<{ username: string }>("/api/v1/me").subscribe({
      next: value => this.me = value.username,
      error: err => console.log(err)
    })

    this.http.get<{role: string}>("/api/v1/admin").subscribe({
      next: value => this.admin = true,
      error: err => console.log(err)
    })

    this.http.get<{role: string}>("/api/v1/user").subscribe({
      next: value => this.user = true,
      error: err => console.log(err)
    })
  }
}
