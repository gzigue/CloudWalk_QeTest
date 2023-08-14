# Quake Log Parser

This project is a Quake log parser that reads game log file generated by a Quake 3 Arena server and prints game statistics to the screen, including a list of players that took part in each game, their
score, and a count of total kills, along with  statistics for cause of death and player ranking.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
  - [Parsing the Log](#parsing-the-log)
- [Testing](#testing)

## Introduction

This project was developed as part of the Quality Engineering test at CloudWalk. It provides a Java-based solution for parsing Quake game log files and extracting information from them.

## Features

- Reads Quake game log files and extracts game data for each match.
- Collects kill data, grouping kills by players and providing a total kill count.
- Supports various death causes (means of death) and provides reports on deaths grouped by death cause.
- Outputs reports for each match and player ranking.

## Getting Started

### Prerequisites

- Java (JDK 8 or higher)
- Maven
- TestNG plugin

### Installation

Clone this repository to your local machine:

```bash 
git clone https://github.com/gzigue/CloudWalk_QeTest.git
```
	
### Usage

### Parsing the Log

To parse the Quake game log file, you just have to run the LogParserMain.jav file s a Java project from your IDE. The path to the log file is already specified in the code.

### Testing

The project includes unit tests written using TestNG. To run the tests, find the testng.xml file in the root folder of the project, and run it as a TestNG suite.