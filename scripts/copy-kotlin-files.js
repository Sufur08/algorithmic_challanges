// Umschreiben des Scripts für ES Module
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

// __dirname Äquivalent für ES Module
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Definiere die Quell- und Zielverzeichnisse
const sourceDir = path.resolve(__dirname, '../kotlin-solutions/src/main/kotlin/solutions');
const targetDir = path.resolve(__dirname, '../public/kotlin');

// Funktion zum rekursiven Erstellen von Verzeichnissen
function ensureDirectoryExists(directory) {
  if (!fs.existsSync(directory)) {
    fs.mkdirSync(directory, { recursive: true });
    console.log(`Verzeichnis erstellt: ${directory}`);
  }
}

// Funktion zum rekursiven Kopieren von Dateien
function copyFilesRecursively(source, target) {
  // Verzeichnis erstellen falls es nicht existiert
  ensureDirectoryExists(target);

  try {
    // Prüfen, ob das Quellverzeichnis existiert
    const sourceStat = fs.statSync(source);
    
    if (!sourceStat.isDirectory()) {
      console.error(`Quellpfad ist kein Verzeichnis: ${source}`);
      return;
    }
    
    // Alle Einträge im Quellverzeichnis lesen
    const entries = fs.readdirSync(source, { withFileTypes: true });

    // Durch alle Einträge iterieren
    for (const entry of entries) {
      const sourcePath = path.join(source, entry.name);
      const targetPath = path.join(target, entry.name);

      // Wenn es ein Verzeichnis ist, rekursiv kopieren
      if (entry.isDirectory()) {
        copyFilesRecursively(sourcePath, targetPath);
      } 
      // Wenn es eine .kt-Datei ist, kopieren
      else if (entry.isFile() && entry.name.endsWith('.kt')) {
        fs.copyFileSync(sourcePath, targetPath);
        console.log(`Datei kopiert: ${sourcePath} -> ${targetPath}`);
      }
    }
  } catch (error) {
    if (error.code === 'ENOENT') {
      console.error(`Quellverzeichnis existiert nicht: ${source}`);
    } else {
      console.error(`Fehler beim Kopieren von Dateien aus ${source}:`, error);
    }
  }
}

// Hauptfunktion
function main() {
  try {
    console.log('Starte das Kopieren von Kotlin-Dateien...');
    console.log(`Quellverzeichnis: ${sourceDir}`);
    console.log(`Zielverzeichnis: ${targetDir}`);
    
    // Überprüfe, ob CoinProblem.kt direkt im solutions-Verzeichnis liegt
    const coinProblemPath = path.join(path.dirname(sourceDir), 'CoinProblem.kt');
    if (fs.existsSync(coinProblemPath)) {
      ensureDirectoryExists(targetDir);
      fs.copyFileSync(coinProblemPath, path.join(targetDir, 'CoinProblem.kt'));
      console.log(`Datei kopiert: ${coinProblemPath} -> ${path.join(targetDir, 'CoinProblem.kt')}`);
    }
    
    copyFilesRecursively(sourceDir, targetDir);
    console.log('Kotlin-Dateien wurden erfolgreich kopiert!');
  } catch (error) {
    console.error('Fehler beim Kopieren der Kotlin-Dateien:', error);
    process.exit(1);
  }
}

// Starte den Kopiervorgang
main();