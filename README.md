# Pawang Digimon - Digimon Tamer Handbook

## Identitas Praktikan
- **Nama** :Nuh Elang Yusarsadam
- **NIM**  : H1D024070   
- **Shift Awal & Baru** : A & D

## Screenshot Aplikasi

### Tema Terang
| Home Screen (Grid List) | Fitur Pencarian (Search) | Detail Screen (Info Lengkap) |
|:---:|:---:|:---:|
| <img width="1080" height="2340" alt="Screenshot_20261009_024458_Pawang Digimon_Home Screen_Terang" src="https://github.com/user-attachments/assets/ca435cb4-b9e1-46bc-b308-176ce72fa5b9" /> | <img width="1080" height="2340" alt="Screenshot_20261009_024550_Pawang Digimon_Search_Terang" src="https://github.com/user-attachments/assets/756f1f34-21b0-48bf-9534-7cf2d16bb5b3" /> | <img width="1080" height="2588" alt="Screenshot_20261009_024653_Pawang Digimon_Detail Screen_Terang" src="https://github.com/user-attachments/assets/d866db84-bcd1-4ec0-b6e4-b4a4edad7cce" /> |

### Tema Gelap
| Home Screen (Grid List) | Fitur Pencarian (Search) | Detail Screen (Info Lengkap) |
|:---:|:---:|:---:|
| <img width="1080" height="2340" alt="Screenshot_20261009_024511_Pawang Digimon_Home Screen_Terang" src="https://github.com/user-attachments/assets/e703a734-4e69-4058-8651-fe59a3ce491e" /> | <img width="1080" height="2340" alt="Screenshot_20261009_024621_Pawang Digimon_Search_Gelap" src="https://github.com/user-attachments/assets/a1f21dac-6814-448f-a5ed-3fc89ee5d318" /> | <img width="1080" height="2588" alt="Screenshot_20261009_024710_Pawang Digimon_Detail Screen_Gelap" src="https://github.com/user-attachments/assets/cbaa378b-9aad-4365-ba70-4eae501db832" /> |

---

## Deskripsi Singkat
**Pawang Digimon (Digimon Tamer Handbook)** adalah aplikasi Android native yang dirancang sebagai buku panduan digital interaktif bagi para Tamer (Pawang Digimon) untuk mengeksplorasi, mengidentifikasi, dan mempelajari berbagai spesies monster.
Aplikasi ini mengambil data secara real-time dari Digi-API (https://digi-api.com/api/v1/digimon), sebuah layanan REST API publik yang memuat kumpulan data lebih dari 1.400 spesies Digimon beserta atribut kuncinya.

---

## Penjelasan Teknis & Arsitektur

Aplikasi ini dibangun menggunakan arsitektur **MVVM (Model-View-ViewModel)** dengan alur data satu arah (*Unidirectional Data Flow / UDF*) dengan struktur:

```
com.pemmob.pawangdigimon/
├── data/
│   ├── model/          # DTO Model Data (DigimonListResponse, DigimonSummary, DigimonDetail, dll.)
│   ├── network/        # Retrofit ApiService & RetrofitInstance Singleton
│   └── repository/     # DigimonRepository untuk abstraksi pemanggilan API & Result handling
├── ui/
│   ├── components/     # Komponen UI Reusable (DigimonCard, LoadingView, ErrorView, EmptyView)
│   ├── navigation/     # Navigation Compose (NavGraph & Route Screen.HOME / Screen.DETAIL)
│   ├── screens/        # Stateless Screen Composables (HomeScreen & DetailScreen)
│   ├── theme/          # Custom Material 3 & Typography
│   └── viewmodel/      # DigimonViewModel & UI State (HomeUiState, DetailUiState)
└── MainActivity.kt     # Single Activity Entry Point
```

### 1. Data Layer (`data/`)
* **`ApiService` & `RetrofitInstance`**: Mengonsumsi REST API dari Digi-API dengan `User-Agent` interceptor dan `HttpLoggingInterceptor` untuk debugging.
* **`DigimonRepository`**: Membungkus hasil pemanggilan API menggunakan `Result<T>` untuk penanganan exception (seperti masalah jaringan/offline).

### 2. ViewModel & State Management (`ui/viewmodel/`)
* **`DigimonViewModel`**: Mengelola state menggunakan `StateFlow` dan coroutines `viewModelScope`.
* **`HomeUiState` & `DetailUiState`**: Menerapkan 3 kondisi UI State utama:
  1. **Loading**: Menampilkan indikator pemuatan data (`LoadingView` / CircularProgressIndicator).
  2. **Success**: Menampilkan data Digimon yang berhasil diunduh.
  3. **Error**: Menampilkan pesan kesalahan lengkap dengan tombol **Coba Lagi (Retry)**.

### 3. UI & Layout Layer (`ui/screens/` & `ui/components/`)
* **Jetpack Compose + Material Design 3**: 100% buatan Compose tanpa XML layout.
* **`LazyVerticalGrid` dengan Infinite Scroll**: Memuat data secara efisien per halaman (*pagination* 30 item/halaman) sehingga aplikasi tetap ringan dan hemat RAM.
* **Server-Side Search**: Mendukung pencarian instan nama atau ID Digimon melintas seluruh database.
* **Coil Image Loading**: Memuat gambar Digimon dari URL dengan dukungan *memory & disk caching* serta transisi *crossfade*.
