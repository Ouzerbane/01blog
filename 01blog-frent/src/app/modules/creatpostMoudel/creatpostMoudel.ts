interface PreviewFile {
  file?: File;       
  url: string;
  type: 'image' | 'video';
  isOld: boolean;   
  id?: string;
}
