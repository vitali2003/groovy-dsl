data <- read.table("data/household_power_consumption.txt", 
	header = TRUE, sep = ";", na.strings = '?',
	colClasses = c('character', 'character', 'numeric', 'numeric', 'numeric', 'numeric', 'numeric', 'numeric', 'numeric'))

data <- data[data[, 'Date'] == '1/2/2007' | data[, 'Date'] == '2/2/2007', ]

hist(data$'Global_active_power', 
	col = "red", main = "Global Active Power", 
	xlab = "Global Active Power (kilowatts)", ylab = "Frequency"))
