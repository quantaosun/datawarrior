package com.actelion.research.datawarrior;

import com.actelion.research.chem.io.CompoundTableConstants;
import com.actelion.research.datawarrior.task.chem.DETaskCalculateChemicalProperties;
import com.actelion.research.table.model.CompoundTableEvent;
import com.actelion.research.table.model.CompoundTableModel;

/**
 * If a column and its content was earlier calculated from other column's data,
 * and if parts of this data is changed, then this class is intended to update
 * the calculated values to again match the underlying data.
 * In general, in DataWarrior tables columns may contain data derived from other columns,
 * which in turn have been calculated from other columns. A calculation algorithm may
 * also have included values from all rows to build a new value for one row. Rows may
 * have been deleted and the order of calculations is unknown. Algorithms may be very
 * complex or data may be missing, e.g. when a column contains a docking score. Thus,
 * in many cases derived data cannot and should not be updated when underlying data changes.
 * This class shall perform these updates only in a few and clearly definable cases:
 * - Update chemical properties, when chemical structures are changed or appended
 */
public class DEDerivedColumnUpdater {
	private final DEFrame mDEFrame;

	public DEDerivedColumnUpdater(DEFrame frame) {
		mDEFrame = frame;
	}

	public void update(CompoundTableEvent cte) {
		if (cte.getType() != CompoundTableEvent.cChangeColumnData) {
			CompoundTableModel tableModel = mDEFrame.getTableModel();
			for (int column=0; column<tableModel.getTotalColumnCount(); column++) {
				String value = tableModel.getColumnProperty(column, CompoundTableConstants.cColumnPropertyCompoundProperty);
				if (value != null) {
					int index = value.indexOf("@");
					if (index != -1) {
						String propertyEncoding = value.substring(0, index);
						int structureColumn = tableModel.findColumn(value.substring(index+1));
						new DETaskCalculateChemicalProperties(mDEFrame, propertyEncoding, structureColumn, column);
					}
				}
			}
		}
	}
}
