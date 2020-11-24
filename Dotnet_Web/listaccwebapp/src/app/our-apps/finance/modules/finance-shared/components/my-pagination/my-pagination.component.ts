import { Component, OnInit, EventEmitter, Input, Output } from '@angular/core';
import { Pagination } from 'src/app/our-apps/finance/models/pagination';

@Component({
  selector: 'app-my-pagination',
  templateUrl: './my-pagination.component.html',
  styleUrls: ['./my-pagination.component.css']
})
export class MyPaginationComponent implements OnInit {

  @Input() pagination: Pagination;
  @Output() pageChanged = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  emitPageChanged(newPageNumber: number) {
    this.pageChanged.emit(newPageNumber);
  }

}
