package net.sourceforge.novaforjava.util;

import net.sourceforge.novaforjava.api.LnEquPosn;

public interface IGetMotionBodyCoords<T> {
	void get_motion_body_coords(double JD, T orbit, LnEquPosn posn);
}
