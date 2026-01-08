## Как использовать

### Создать виртуальное окружение

```bash
py -3.10 -m venv venv
```

### Активировать окружение
Windows (PowerShell):
```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\venv\Scripts\Activate.ps1
```
Linux / macOS:
```bash
source venv/bin/activate
```

### Установить зависимости

```bash
python -m pip install --upgrade pip
```
```bash
pip install -r requirements.txt
```

### Запуск обучения

```bash
python train.py
```

## После выполнения:

- Модель будет сохранена в artifacts/credit_model.onnx

- В консоль выведутся метрики качества.