import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  ngOnInit() {
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach(link => {
      if(window.location.href.endsWith(<string>link.getAttribute('href')) || '') {
        link.classList.add('active');
      }
      link.addEventListener('click', (event) => {
        linkColor.forEach(l => {
          l.classList.remove('active');
          link.classList.add('active');
        })
      });
    });
  }

  logout() {

  }
}
