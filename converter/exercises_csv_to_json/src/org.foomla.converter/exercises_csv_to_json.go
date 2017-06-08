package main

import (
	"encoding/csv"
	"encoding/json"
	"io"
	"io/ioutil"
	"log"
	"os"
	"strconv"
	"strings"
	"time"
)

const (
	DEFAULT_CSV_EXERCISES  = "/opt/foomla/exercises.csv"
	DEFAULT_JSON_EXERCISES = "/opt/foomla/exercises.json"

	TIMESTAMP_LAYOUT = "2.1.2006 15:04:05"
)

var AGE_CLASS_MAPPING = map[string]string{
	"MINI":   "MINI",
	"U9":     "U9",
	"U11":    "U11",
	"U13":    "U13",
	"U15":    "U15",
	"U17":    "U17",
	"U19":    "U19",
	"SENIOR": "SENIOR",
	"Senior": "SENIOR"}

var AGE_CLASS = []string{
	"MINI",
	"U9",
	"U11",
	"U13",
	"U15",
	"U17",
	"U19",
	"SENIOR"}

var TRAINING_PHASE_MAPPING = map[string]string{
	"Aufwärmphase1": "ARRIVAL",
	"Warm-up 1":     "ARRIVAL",
	"Aufwärmphase2": "WARM_UP",
	"Warm-up 2":     "WARM_UP",
	"Hauptteil":     "MAIN",
	"Main":          "MAIN",
	"Abschluss":     "SCRIMMAGE",
	"Final":         "SCRIMMAGE"}

var TRAINING_PHASE = []string{
	"ARRIVAL",
	"WARM_UP",
	"MAIN",
	"SCRIMMAGE"}

var TRAINING_FOCUS_MAPPING = map[string]string{
	"Kondition": "ENDURANCE",
	"Endurance": "ENDURANCE",
	"Taktik":    "TACTIC",
	"Tactics":   "TACTIC",
	"Technik":   "TECHNIC",
	"Technique": "TECHNIC",
	"Mental":    "MENTAL",
	"Wissen":    "KNOWLEDGE",
	"Knowledge": "KNOWLEDGE"}

var TRAINING_FOCUS = []string{
	"ENDURANCE",
	"TACTIC",
	"TECHNIC",
	"MENTAL",
	"KNOWLEDGE"}

var STATUS = []string{
	"DRAFT",
	"REVISE",
	"PUBLISHED",
	"DISABLED"}

type Error struct {
	Field string
	Value string
}

type Exercise struct {
	CreatedAt        int64    `json:"createdAt"`
	Title            string   `json:"title"`
	TrainingPhases   []string `json:"trainingPhases"`
	TrainingFocus    string   `json:"trainingFocus"`
	AgeClasses       []string `json:"ageClasses"`
	Setting          string   `json:"setting"`
	Schedule         string   `json:"schedule"`
	Objective        string   `json:"objective"`
	AuxileryMaterial string   `json:"auxiliaryMaterial"`
	Note             string   `json:"note"`
	MinPlayers       int      `json:"minPlayers"`
	Status           string   `json:"exerciseStatus"`
	Images           []string `json:"images"`
}

type Response2 struct {
	Page   int      `json:"page"`
	Fruits []string `json:"fruits"`
}

func main() {
	csvFile, jsonFile := parseConfiguration()

	log.Println("Read  ->", csvFile)
	log.Println("Write ->", jsonFile)

	csvData, _ := ioutil.ReadFile(csvFile)

	exercises := make([]Exercise, 0)

	readCsvExercises(csvData, &exercises)
	writeJsonExercises(jsonFile, &exercises)
}

func parseConfiguration() (string, string) {
	csvFile := DEFAULT_CSV_EXERCISES
	jsonFile := DEFAULT_JSON_EXERCISES

	if len(os.Args) >= 2 && strings.Index(os.Args[1], ".csv") > 0 {
		csvFile = os.Args[1]
	} else if len(os.Args) >= 2 && strings.Index(os.Args[1], ".json") > 0 {
		jsonFile = os.Args[1]
	}

	if len(os.Args) >= 3 && strings.Index(os.Args[2], ".csv") > 0 {
		csvFile = os.Args[2]
	} else if len(os.Args) >= 3 && strings.Index(os.Args[2], ".json") > 0 {
		jsonFile = os.Args[2]
	}

	return csvFile, jsonFile
}

func readCsvExercises(data []byte, exercises *[]Exercise) int {
	csvReader := csv.NewReader(strings.NewReader(string(data)))

	exercisesRead := 0
	fileLine := -1

	for {
		fileLine = fileLine + 1

		record, err := csvReader.Read()
		if err == io.EOF {
			break
		}

		if fileLine == 0 {
			continue
		}

		if err != nil {
			log.Fatal(err)
		}

		readErrors := readExercise(exercises, record)

		if len(readErrors) > 0 {
			log.Printf("Failed parsing exercise")
			for _, readError := range readErrors {
				log.Printf("\t- Line  : %d", fileLine)
				log.Printf("\t  Field : %s", readError.Field)
				log.Printf("\t  Value : %s", readError.Value)
			}
		} else {
			exercisesRead = exercisesRead + 1
		}
	}

	log.Printf("Read %d exercises\n", exercisesRead)

	return exercisesRead
}

func readExercise(exercises *[]Exercise, cols []string) []Error {
	createdAt, _ := time.Parse(TIMESTAMP_LAYOUT, cols[0])
	minPlayers, _ := strconv.ParseInt(cols[11], 10, 64)
	trainingPhases := strings.Split(cols[2], ",")
	ageClasses := strings.Split(cols[4], ",")

	exercise := Exercise{
		CreatedAt:        createdAt.Unix(),
		Title:            cols[1],
		TrainingPhases:   mapValues(TRAINING_PHASE_MAPPING, trainingPhases),
		TrainingFocus:    mapValue(TRAINING_FOCUS_MAPPING, cols[3]),
		AgeClasses:       mapValues(AGE_CLASS_MAPPING, ageClasses),
		Setting:          cols[5],
		Schedule:         cols[6],
		Objective:        cols[7],
		AuxileryMaterial: cols[8],
		Note:             cols[9],
		Images:           []string{cols[10]},
		MinPlayers:       int(minPlayers),
		Status:           cols[12]}

	errors := checkForErrors(exercise)
	if len(errors) > 0 {
		return errors
	} else {
		*exercises = append(*exercises, exercise)
		return nil
	}
}

func mapValues(mapping map[string]string, values []string) []string {
	mappedValues := make([]string, 0)

	for _, value := range values {
		mappedValues = append(mappedValues, mapValue(mapping, value))
	}

	return mappedValues
}

func mapValue(mapping map[string]string, value string) string {
	value = strings.TrimSpace(value)
	mapped, ok := mapping[value]

	if ok {
		return mapped
	} else {
		return value
	}
}

func writeJsonExercises(jsonFile string, exercises *[]Exercise) {
	log.Printf("Start writing %d exercises to json", len(*exercises))
	jsonEx, _ := json.Marshal(*exercises)

	ioutil.WriteFile(jsonFile, jsonEx, 0644)
}

func checkForErrors(exercise Exercise) []Error {
	errors := make([]Error, 0)

	if strings.EqualFold(exercise.Title, "") {
		errors = append(errors, Error{
			Field: "Title",
			Value: exercise.Title})
	}
	if strings.EqualFold(exercise.Schedule, "") {
		errors = append(errors, Error{
			Field: "Schedule",
			Value: exercise.Schedule})
	}
	if exercise.MinPlayers < 0 {
		errors = append(errors, Error{
			Field: "MinPlayers",
			Value: strconv.Itoa(exercise.MinPlayers)})
	}
	if len(exercise.Images) > 0 && strings.Index(exercise.Images[0], "http://") != 0 {
		errors = append(errors, Error{
			Field: "ImageUrl",
			Value: strings.Join(exercise.Images, ",")})
	}
	if !sliceContainsValue(TRAINING_FOCUS, exercise.TrainingFocus) {
		errors = append(errors, Error{
			Field: "TrainingFocus",
			Value: exercise.TrainingFocus})
	}
	if !sliceContainsValues(TRAINING_PHASE, exercise.TrainingPhases) {
		errors = append(errors, Error{
			Field: "TrainingPhase",
			Value: strings.Join(exercise.TrainingPhases, ",")})
	}
	if !sliceContainsValues(AGE_CLASS, exercise.AgeClasses) {
		errors = append(errors, Error{
			Field: "AgeClasses",
			Value: strings.Join(exercise.AgeClasses, ",")})
	}
	if !sliceContainsValue(STATUS, exercise.Status) {
		errors = append(errors, Error{
			Field: "Status",
			Value: exercise.Status})
	}

	return errors
}

func sliceContainsValues(slice []string, values []string) bool {
	for _, value := range values {
		if !sliceContainsValue(slice, value) {
			return false
		}
	}

	return true
}

func sliceContainsValue(slice []string, value string) bool {
	for _, item := range slice {
		if item == value {
			return true
		}
	}

	return false
}
