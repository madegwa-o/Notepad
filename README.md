# Anaconda Installation Guide (Ubuntu → /opt)

This document provides a complete guide to installing **Anaconda** in a system directory (`/opt/anaconda3`) on Ubuntu-based systems.

---

## 📌 Overview

This setup installs Anaconda in `/opt` instead of the default home directory. It is useful for:

* System-wide installations
* Multi-user environments
* Better filesystem organization

---

## ⚙️ Prerequisites

Ensure your system has:

* Ubuntu / Debian-based OS
* `bash` shell
* `wget` or `curl`
* `sudo` privileges

Update your system:

```bash
sudo apt update && sudo apt upgrade -y
```

---

## ⬇️ Download Installer

```bash
cd ~/Downloads
wget https://repo.anaconda.com/archive/Anaconda3-2025.12-2-Linux-x86_64.sh
```

---

## 🔐 (Optional) Verify Installer

```bash
sha256sum Anaconda3-2025.12-2-linux-x86_64.sh
```

Compare with the checksum from the official Anaconda website.

---

## 🚀 Install Anaconda to `/opt`

Run the installer:

```bash
sudo bash Anaconda3-latest-Linux-x86_64.sh
```

### During installation:

* Press `Enter` to scroll
* Type `yes` to accept the license
* Set installation path to:

```bash
/opt/anaconda3
```

---

## 🔧 Fix Permissions

Allow your user to manage Anaconda without `sudo`:

```bash
sudo chown -R $USER:$USER /opt/anaconda3
```

---

## 🔁 Initialize Conda

```bash
/opt/anaconda3/bin/conda init
source ~/.bashrc
```

---

## ✅ Verify Installation

```bash
conda --version
which conda
```

Expected output:

```
/opt/anaconda3/bin/conda
```

---

## 🧪 Test Installation

```bash
conda create -n test_env python=3.10 -y
conda activate test_env
python --version
```

---

## 📦 Environment Management

Create a development environment:

```bash
conda create -n ai_env python=3.10
conda activate ai_env
```

Install packages:

```bash
pip install opencv-python ultralytics flask
```

---

## 👥 Multi-User Setup (Optional)

```bash
sudo chgrp -R users /opt/anaconda3
sudo chmod -R 775 /opt/anaconda3
```

> Do not change ownership if multiple users will use Anaconda.

---

## 🛠️ Troubleshooting

### `conda: command not found`

```bash
source /opt/anaconda3/bin/activate
conda init
```

---

### Permission Issues

```bash
sudo chown -R $USER:$USER /opt/anaconda3
```

---

### Wrong Python Version

```bash
conda activate <env_name>
which python
```

---

## 🗑️ Uninstall

```bash
sudo rm -rf /opt/anaconda3
```

Remove conda initialization from `~/.bashrc`, then:

```bash
source ~/.bashrc
```

---

## 💡 Notes

* Avoid using the `base` environment for development
* Prefer isolated environments per project
* Avoid mixing `pip` and `conda` unnecessarily

---

## 📚 Alternative

For a lighter setup, consider **Miniconda**.

---

## 📄 License

This guide is provided for educational and development use.
