import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBook, Book } from 'app/shared/model/book.model';
import { BookService } from './book.service';

@Component({
  selector: 'jhi-book-update',
  templateUrl: './book-update.component.html'
})
export class BookUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    year: [null, [Validators.required, Validators.min(0)]],
    page: [null, [Validators.required, Validators.min(1)]]
  });

  constructor(protected bookService: BookService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ book }) => {
      this.updateForm(book);
    });
  }

  updateForm(book: IBook): void {
    this.editForm.patchValue({
      id: book.id,
      name: book.name,
      year: book.year,
      page: book.page
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const book = this.createFromForm();
    if (book.id !== undefined) {
      this.subscribeToSaveResponse(this.bookService.update(book));
    } else {
      this.subscribeToSaveResponse(this.bookService.create(book));
    }
  }

  private createFromForm(): IBook {
    return {
      ...new Book(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      year: this.editForm.get(['year'])!.value,
      page: this.editForm.get(['page'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBook>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
