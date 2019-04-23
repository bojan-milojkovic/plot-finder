
CREATE INDEX PriceIndex ON plotfinder.plot(price);
CREATE INDEX SizeIndex ON plotfinder.plot(size);
CREATE INDEX CityIndex ON plotfinder.plot(city);
CREATE INDEX DistrictIndex ON plotfinder.plot(district);

CREATE INDEX FlagsIndex ON plotfinder.flags(plot_id);

CREATE INDEX ll_x_index ON plotfinder.watched_area(ll_x);
CREATE INDEX ll_y_index ON plotfinder.watched_area(ll_y);
CREATE INDEX ur_x_index ON plotfinder.watched_area(ur_x);
CREATE INDEX ur_y_index ON plotfinder.watched_area(ur_y);