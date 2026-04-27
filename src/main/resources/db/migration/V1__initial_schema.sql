CREATE TABLE IF NOT EXISTS weather_forecasts(
    id SERIAL PRIMARY KEY,
    forecast_date DATE NOT NULL,
    rain_expected_mm DOUBLE PRECISION NOT NULL,
    max_temp_celsius DOUBLE PRECISION NOT NULL,
    fetched_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS rain_history(
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    rain_mm DOUBLE PRECISION NOT NULL,
    fetched_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS irrigation_advices(
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    duration_minutes INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS irrigation_events(
    id SERIAL PRIMARY KEY,
    event_date TIMESTAMP NOT NULL,
    duration_minutes INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL
);
