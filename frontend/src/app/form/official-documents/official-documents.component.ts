import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AutoSaveService } from '../../services/autosave.service';
import { debounceTime } from 'rxjs';

interface FilePreview {
  file: File | null;
  url: string | null;
}

@Component({
  selector: 'app-official-documents',
  templateUrl: './official-documents.component.html',
  styleUrls: ['./official-documents.component.css']
})
export class OfficialDocumentsComponent implements OnInit {
  @Output() nextStep = new EventEmitter<void>();

  documentsForm!: FormGroup;

  diplomaPreview: FilePreview = { file: null, url: null };
  bacPreview: FilePreview = { file: null, url: null };
  cniRectoPreview: FilePreview = { file: null, url: null };
  cniVersoPreview: FilePreview = { file: null, url: null };
  birthCertPreview: FilePreview = { file: null, url: null };
  photoPreview: FilePreview = { file: null, url: null };

  constructor(private formBuilder: FormBuilder, private autoSave: AutoSaveService) {}

  ngOnInit(): void {
    this.documentsForm = this.formBuilder.group({
      diploma: [null, [Validators.required]],
      baccalaureat: [null, [Validators.required]],
      cniRecto: [null, [Validators.required]],
      cniVerso: [null, [Validators.required]],
      birthCertificate: [null, [Validators.required]],
      identityPhoto: [null, [Validators.required]]
    });

    const cached = this.autoSave.load<any>('step_documents');
    if (cached) {
      this.documentsForm.patchValue(cached);
    }

    this.documentsForm.valueChanges.pipe(debounceTime(300)).subscribe(v => this.autoSave.save('step_documents', v));
  }

  onFileSelected(event: Event, controlName: string, acceptImagesOnly: boolean = false, acceptPdfOnly: boolean = false): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) {
      return;
    }
    const file = input.files[0];

    if (acceptImagesOnly && !/^image\//.test(file.type)) {
      this.documentsForm.get(controlName)?.setErrors({ mime: true });
      return;
    }
    if (acceptPdfOnly && file.type !== 'application/pdf') {
      this.documentsForm.get(controlName)?.setErrors({ mime: true });
      return;
    }
    if (file.size > 5 * 1024 * 1024) {
      this.documentsForm.get(controlName)?.setErrors({ size: true });
      return;
    }

    this.documentsForm.get(controlName)?.setValue(file.name);

    const previewKey = this.mapControlToPreview(controlName);
    if (!previewKey) return;

    const previewTarget = (this as any)[previewKey] as FilePreview;
    previewTarget.file = file;

    if (acceptImagesOnly) {
      const reader = new FileReader();
      reader.onload = () => {
        previewTarget.url = reader.result as string;
      };
      reader.readAsDataURL(file);
    } else {
      previewTarget.url = null; // no preview for pdf
    }
  }

  private mapControlToPreview(controlName: string): string | null {
    switch (controlName) {
      case 'diploma':
        return 'diplomaPreview';
      case 'baccalaureat':
        return 'bacPreview';
      case 'cniRecto':
        return 'cniRectoPreview';
      case 'cniVerso':
        return 'cniVersoPreview';
      case 'birthCertificate':
        return 'birthCertPreview';
      case 'identityPhoto':
        return 'photoPreview';
      default:
        return null;
    }
  }

  onSubmit(): void {
    if (this.documentsForm.invalid) {
      this.documentsForm.markAllAsTouched();
      return;
    }
    this.autoSave.save('step_documents', this.documentsForm.value);
    this.nextStep.emit();
  }
}
