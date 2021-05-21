import { Component, OnInit } from '@angular/core';
import { IPhoto } from './photo';
import { PhotoService } from './photo.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'client';
  photos: IPhoto[] = [];
  page: number = 0;
  size: number = 12;
  imagePath: string;
  constructor(private photoService: PhotoService) { }

  ngOnInit() {
    this.getAllPhotos(this.page, this.size);
  }

  getAllPhotos(page: number, size: number) {
    this.photoService.getAllPhotos(page, size)
    .subscribe(response => this.photos = response);
  }

  showModal(event){
    console.log(event)
    this.imagePath = event.srcElement.currentSrc;
  }
  
  getNext(){
    this.page += 1;
    this.getAllPhotos(this.page, this.size);
  }

  getPrevious(){
    if(this.page > 0){
      this.page -= 1;
      this.getAllPhotos(this.page, this.size);
    }
  }
}
