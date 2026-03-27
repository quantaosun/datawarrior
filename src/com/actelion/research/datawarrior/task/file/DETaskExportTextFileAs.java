/*
 * Copyright 2017 Idorsia Pharmaceuticals Ltd., Hegenheimermattweg 91, CH-4123 Allschwil, Switzerland
 *
 * This file is part of DataWarrior.
 * 
 * DataWarrior is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * DataWarrior is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with DataWarrior.
 * If not, see http://www.gnu.org/licenses/.
 *
 * @author Thomas Sander
 */

package com.actelion.research.datawarrior.task.file;

import com.actelion.research.datawarrior.DEFrame;
import com.actelion.research.gui.FileHelper;
import com.actelion.research.table.CompoundTableSaver;
import com.actelion.research.table.model.CompoundTableModel;

import javax.swing.*;
import java.io.File;
import java.util.Properties;

public class DETaskExportTextFileAs extends DETaskAbstractSaveFile {
    public static final String TASK_NAME_ALL_TXT = "Export Text-File";
	public static final String TASK_NAME_ALL_CSV = "Export CSV-File";
	public static final String TASK_NAME_SEL_TXT = "Export Selection As Text-File";
	public static final String TASK_NAME_SEL_CSV = "Export Selection As CSV-File";
	public static final String TASK_NAME_VIS_TXT = "Export Visible Rows As Text-File";
	public static final String TASK_NAME_VIS_CSV = "Export Visible Rows As CSV-File";

	private final int mFileType;
	private final long mRowMask;

	public DETaskExportTextFileAs(DEFrame parent, int fileType, long rowMask) {
		super(parent, getTaskName(fileType, rowMask));
		mFileType = fileType;
		mRowMask = rowMask;
		}

	public static String getTaskName(int fileType, long rowMask) {
		return (fileType == FileHelper.cFileTypeTextCommaSeparated) ?
				  ((rowMask == CompoundTableSaver.ROW_MASK_ALL) ? TASK_NAME_ALL_CSV
				 : (rowMask == CompoundTableSaver.ROW_MASK_VISIBLE) ? TASK_NAME_VIS_CSV : TASK_NAME_SEL_CSV)
				: ((rowMask == CompoundTableSaver.ROW_MASK_ALL) ? TASK_NAME_ALL_TXT
				 : (rowMask == CompoundTableSaver.ROW_MASK_VISIBLE) ? TASK_NAME_VIS_TXT : TASK_NAME_SEL_TXT);
		}

	@Override
	public String getTaskName() {
		return getTaskName(mFileType, mRowMask);
		}

	@Override
	public int getFileType() {
		return mFileType;
		}

	@Override
	public JComponent createInnerDialogContent() {
		return null;
		}

	@Override
	public boolean isConfigurable() {
		if (getTableModel().getTotalRowCount() == 0
		 || getTableModel().getTotalColumnCount() == 0) {
			showErrorMessage("Empty documents cannot be saved.");
			return false;
			}
		return true;
		}

	@Override
	public void saveFile(File file, Properties configuration) {
		CompoundTableModel tableModel = ((DEFrame)getParentFrame()).getMainFrame().getTableModel();
		JTable table = ((DEFrame)getParentFrame()).getMainFrame().getMainPane().getTable();
		new CompoundTableSaver(getParentFrame(), tableModel, table).saveText(file, mRowMask);
		}
	}
